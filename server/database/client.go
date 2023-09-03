package database

import (
	"context"

	"cloud.google.com/go/firestore"
)

var dbClient *firestore.Client

const projectId = "simon-duchastel-habit-tracker"

func GetOrCreateDbClient(ctx context.Context) (*firestore.Client, error) {
	if dbClient != nil {
		return dbClient, nil
	}

	newClient, err := firestore.NewClient(ctx, projectId)
	if err != nil {
		return nil, err
	} else {
		dbClient = newClient
		return newClient, nil
	}
}
