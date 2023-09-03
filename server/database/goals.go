package database

import (
	"context"
	"fmt"
	"time"

	"cloud.google.com/go/firestore"
	"google.golang.org/api/iterator"

	log "github.com/simon-duchastel/habit-tracker/server/utils"
)

const goalsCollection = "goals"

const goalsKeySchemaVersion = "schemaVersion"
const goalsKeyGoalId = "goalId"
const goalsKeyStartDate = "startDate"
const goalsKeyEndDate = "endDate"
const goalsKeyTitle = "title"
const goalsKeyActiveOn = "activeOn"
const goalsKeyUser = "userId"

func GetGoalsActiveInRange(
	ctx context.Context,
	client *firestore.Client,
	userId string,
	start time.Time,
	end time.Time,
) ([]Goal, error) {

	// this query should check that goal.endDate >= end && goal.startDate <= start
	// however, due to performance limitations in the database (firestore), we can
	// only check 1 field. We choose to check goal.endDate and filter out goals
	// that weren't yet started in the client code
	iter := client.Collection(goalsCollection).
		Where(goalsKeyUser, "==", userId).
		Where(goalsKeySchemaVersion, "==", "v1").
		WhereEntity(firestore.OrFilter{
			Filters: []firestore.EntityFilter{
				// if the end date is nil, we know the goal's end date must come after
				// 'end' because it's still active now
				firestore.PropertyFilter{
					Path:     goalsKeyEndDate,
					Operator: "==",
					Value:    nil,
				},
				// the goal's end date must come after 'end'
				firestore.PropertyFilter{
					Path:     goalsKeyEndDate,
					Operator: ">",
					Value:    end.AddDate(0, 0, -1), // subtract one to make the check inclusive
				},
			},
		}).
		Documents(ctx)

	var goals = []Goal{}
	for {
		doc, err := iter.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			log.LogError(fmt.Sprintf("Received error retrieving goal: %v", err))
			return nil, err
		}

		// parse the database entry
		var dbEntry dbGoal
		if err = doc.DataTo(&dbEntry); err != nil {
			log.LogError(fmt.Sprintf("Received error parsing goal entry from database: %v", err))
			return nil, err
		}

		// skip goals where the start time is after the end of our range
		// the query already took care of ommitting goals that end before the start of our range)
		if dbEntry.StartDate.After(end) {
			continue // this goal's start date is after the end of our range - skip it
		}

		goals = append(goals, Goal{
			ActiveOn:  dbEntry.ActiveOn,
			Title:     dbEntry.Title,
			GoalId:    dbEntry.GoalId,
			StartDate: dbEntry.StartDate,
			EndDate:   dbEntry.EndDate,
		})
	}
	return goals, nil
}

type dbGoal struct {
	StartDate     time.Time  `firestore:"startDate,omitempty"`
	EndDate       *time.Time `firestore:"endDate,omitempty"`
	GoalId        string     `firestore:"goalId,omitempty"`
	Title         string     `firestore:"title,omitempty"`
	ActiveOn      []bool     `firestore:"activeOn,omitempty"`
	SchemaVersion string     `firestore:"schemaVersion,omitempty"`
	UserId        string     `firestore:"userId,omitempty"`
}

type Goal struct {
	GoalId    string
	StartDate time.Time
	EndDate   *time.Time // nil if goal is active
	Title     string
	ActiveOn  []bool // index is equal to time.Weekday (ie. day of week, 0 = Sunday, 6 = Saturday)
}
