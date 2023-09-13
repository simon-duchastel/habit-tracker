package database

import (
	"context"

	"cloud.google.com/go/firestore"
)

const projectId = "simon-duchastel-habit-tracker"

func GetDbClient(ctx context.Context) (*firestore.Client, error) {
	newClient, err := firestore.NewClient(ctx, projectId)
	if err != nil {
		return nil, err
	}
	return newClient, nil
}
