package models

import (
	"encoding/json"
	"reflect"
	"testing"
)

func TestHabitsForDayRequestV1(t *testing.T) {
	requestJson := []byte(`{
		"completed": [
			{
				"title": "complete-goal",
				"goalId": "goal-12"
			},
			{
				"title": "second-complete-goal",
				"goalId": "goal-34"
			}
		],
		"uncompleted": [
			{
				"title": "incomplete-goal",
				"goalId": "goal-56"
			},
			{
				"title": "second-incomplete-goal",
				"goalId": "goal-78"
			}
		]
	}`)

	var request HabitsForDayRequestV1
	err := json.Unmarshal(requestJson, &request)
	if err != nil {
		t.Fatalf("Failed to parse json")
	}
	expectedRequest := HabitsForDayRequestV1{
		Completed: []Goal{
			{
				Title:  "complete-goal",
				GoalId: "goal-12",
			},
			{
				Title:  "second-complete-goal",
				GoalId: "goal-34",
			},
		},
		Uncompleted: []Goal{
			{
				Title:  "incomplete-goal",
				GoalId: "goal-56",
			},
			{
				Title:  "second-incomplete-goal",
				GoalId: "goal-78",
			},
		},
	}

	if !reflect.DeepEqual(request, expectedRequest) {
		t.Fatalf("Request did not match expected value:\n\texpected \"%v\"\n\tgot      \"%v\"", expectedRequest, request)
	}
}
