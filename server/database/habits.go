package database

import (
	"context"
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
		Where(habitsKeyDate, ">", start.AddDate(0, 0, -1)). // subtract one to make the check inclusive
		Where(habitsKeyDate, "<", end.AddDate(0, 0, 1)).    // add 1 to make the check inclusive
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

		var dbEntry dbHabit
		if err = doc.DataTo(&dbEntry); err != nil {
			log.LogError(fmt.Sprintf("Received error parsing habit entry from database: %v", err))
			return nil, err
		}

		days = append(days, HabitDay{
			Day:         dbEntry.Date,
			Completed:   dbEntry.Completed,
			Uncompleted: dbEntry.Uncompleted,
		})
	}
	return days, nil
}

type dbHabit struct {
	Date          time.Time `firestore:"date,omitempty"`
	Completed     []string  `firestore:"completed,omitempty"`
	Uncompleted   []string  `firestore:"uncompleted,omitempty"`
	SchemaVersion string    `firestore:"schemaVersion,omitempty"`
	UserId        string    `firestore:"userId,omitempty"`
}

type HabitDay struct {
	Day         time.Time
	Completed   []string
	Uncompleted []string
}
