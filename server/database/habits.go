package database

import (
	"context"
	"errors"
	"time"

	"cloud.google.com/go/firestore"
	"google.golang.org/api/iterator"
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

	iter := client.Collection(habitsCollection).
		Where(habitsKeyUser, "==", userId).
		Where(habitsKeySchemaVersion, "==", "v1").
		Where(habitsKeyDate, "<=", start).
		Where(habitsKeyDate, ">=", end).
		Documents(ctx)

	var days = []HabitDay{}
	for {
		doc, err := iter.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			return nil, err
		}

		var day time.Time
		var completed []string
		var uncompleted []string
		var ok bool
		if day, ok = doc.Data()[habitsKeyDate].(time.Time); ok {
			return nil, errors.New("")
		}
		if completed, ok = doc.Data()[habitsKeyCompleted].([]string); ok {
			return nil, errors.New("")
		}
		if uncompleted, ok = doc.Data()[habitsKeyUncompleted].([]string); ok {
			return nil, errors.New("")
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
