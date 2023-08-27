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
				"goalId": "goal-12"
			},
			{
				"goalId": "goal-34"
			}
		],
		"uncompleted": [
			{
				"goalId": "goal-56"
			},
			{
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
		Completed: []GoalId{
			{
				GoalId: "goal-12",
			},
			{
				GoalId: "goal-34",
			},
		},
		Uncompleted: []GoalId{
			{
				GoalId: "goal-56",
			},
			{
				GoalId: "goal-78",
			},
		},
	}

	if !reflect.DeepEqual(request, expectedRequest) {
		t.Fatalf("Request did not match expected value:\n\texpected \"%v\"\n\tgot      \"%v\"", expectedRequest, request)
	}
}
