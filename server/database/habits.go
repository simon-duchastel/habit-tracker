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

const habitsCollection = "habits"

const habitsKeySchemaVersion = "schemaVersion"
const habitsKeyDate = "date"
const habitsKeyCompleted = "completed"
const habitsKeyUncompleted = "uncompleted"
const habitsKeyUser = "userId"

func GetHabitRange(
	ctx context.Context,
	client *firestore.Client,
	userId string,
	start time.Time,
	end time.Time,
) ([]HabitDay, error) {

	// Check that the start <= habit.date <= end
	iter := client.Collection(habitsCollection).
		Where(habitsKeyUser, "==", userId).
		Where(habitsKeySchemaVersion, "==", "v1").
		Where(habitsKeyDate, ">=", start).
		Where(habitsKeyDate, "<=", end).
		Documents(ctx)

	var days = []HabitDay{}
	for {
		doc, err := iter.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			log.LogError(fmt.Sprintf("Received error retrieving habit: %v", err))
			return nil, err
		}

		log.LogInfo(fmt.Sprintf("Data: %v", doc.Data()))

		var day time.Time
		var completed []string
		var uncompleted []string
		var ok bool
		if day, ok = doc.Data()[habitsKeyDate].(time.Time); !ok {
			errString := fmt.Sprintf("Received error parsing 'date': %v", err)
			return nil, errors.New(errString)
		}
		if completed, ok = doc.Data()[habitsKeyCompleted].([]string); !ok {
			errString := fmt.Sprintf("Received error parsing 'completed': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}
		if uncompleted, ok = doc.Data()[habitsKeyUncompleted].([]string); !ok {
			errString := fmt.Sprintf("Received error parsing 'uncompleted': %v", err)
			log.LogError(errString)
			return nil, errors.New(errString)
		}

		days = append(days, HabitDay{
			Day:         day,
			Completed:   completed,
			Uncompleted: uncompleted,
		})
	}
	return days, nil
}

type HabitDay struct {
	Day         time.Time
	Completed   []string
	Uncompleted []string
}
