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

	iter := client.Collection(goalsCollection).
		Where(goalsKeyUser, "==", userId).
		Where(goalsKeySchemaVersion, "==", "v1").
		Where(goalsKeyStartDate, ">=", start).
		WhereEntity(firestore.OrFilter{
			Filters: []firestore.EntityFilter{
				firestore.PropertyFilter{
					Path:     goalsKeyEndDate,
					Operator: "==",
					Value:    nil,
				},
				firestore.PropertyFilter{
					Path:     goalsKeyEndDate,
					Operator: "<=",
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

		var activeOn []bool
		var title string
		var goalId string
		var startDate time.Time
		var endDate *time.Time = nil
		var ok bool
		if activeOn, ok = doc.Data()[goalsKeyActiveOn].([]bool); ok {
			errString := fmt.Sprintf("Received error parsing 'activeOn': %v", err)
			log.LogError(errString)
			return nil, errors.New("")
		}
		if title, ok = doc.Data()[goalsKeyTitle].(string); ok {
			errString := fmt.Sprintf("Received error parsing 'title': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}
		if goalId, ok = doc.Data()[goalsKeyGoalId].(string); ok {
			errString := fmt.Sprintf("Received error parsing 'goalId': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}
		if startDate, ok = doc.Data()[goalsKeyStartDate].(time.Time); ok {
			errString := fmt.Sprintf("Received error parsing 'startDate': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}
		var endDateRaw interface{}
		endDateRaw, ok = doc.Data()[goalsKeyEndDate]
		if ok {
			// if it exists, check its type and assign it to endDay.
			// Otherwise, leave the default value of nil
			var endDateRef time.Time
			if endDateRef, ok = endDateRaw.(time.Time); ok {
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
