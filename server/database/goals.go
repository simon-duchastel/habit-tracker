package database

import (
	"context"
	"errors"
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
					Operator: ">=",
					Value:    end,
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

		log.LogInfo(fmt.Sprintf("Data: %v", doc.Data()))

		// parse the start date and skip goals that start after our range
		var startDate time.Time
		var ok bool
		if startDate, ok = doc.Data()[goalsKeyStartDate].(time.Time); !ok {
			errString := fmt.Sprintf("Received error parsing 'startDate': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}
		if startDate.After(end) {
			continue // this goal's start date is after the end of our range - skip it
		}

		// parse remaining fields
		var activeOn []bool
		var title string
		var goalId string
		var endDate *time.Time = nil
		if activeOn, ok = doc.Data()[goalsKeyActiveOn].([]bool); !ok {
			errString := fmt.Sprintf("Received error parsing 'activeOn': %v", err)
			log.LogError(errString)
			return nil, errors.New("")
		}
		if title, ok = doc.Data()[goalsKeyTitle].(string); !ok {
			errString := fmt.Sprintf("Received error parsing 'title': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}
		if goalId, ok = doc.Data()[goalsKeyGoalId].(string); !ok {
			errString := fmt.Sprintf("Received error parsing 'goalId': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}
		var endDateRaw interface{}
		endDateRaw, ok = doc.Data()[goalsKeyEndDate]
		if ok {
			// if it exists, check its type and assign it to endDay.
			// Otherwise, leave the default value of nil
			var endDateRef time.Time
			if endDateRef, ok = endDateRaw.(time.Time); !ok {
				errString := fmt.Sprintf("Received error parsing 'endDate': %v", err)
				log.LogError(errString)
				return nil, errors.New(errString)
			} else {
				endDate = &endDateRef
			}
		}

		goals = append(goals, Goal{
			ActiveOn:  activeOn,
			Title:     title,
			GoalId:    goalId,
			StartDate: startDate,
			EndDate:   endDate,
		})
	}
	return goals, nil
}

type Goal struct {
	GoalId    string
	StartDate time.Time
	EndDate   *time.Time // nil if goal is active
	Title     string
	ActiveOn  []bool // index is equal to time.Weekday (ie. day of week, 0 = Sunday, 6 = Saturday)
}
