package models

import (
	"encoding/json"
	"testing"
)

func TestHabitsSummaryResponseV1(t *testing.T) {
	expectedJson := []byte(`{"habits":[{"date":"2023-08-26","missedHabit":true,"completed":["goal-1","goal-2"],"uncompleted":["goal-3","goal-4","goal-5"]},{"date":"2023-08-25","missedHabit":false,"completed":["goal-1","goal-2","goal-3"],"uncompleted":["goal-4","goal-5","goal-6"]}]}`)

	var response HabitsSummaryResponseV1
	response.Habits = []HabitSummary{
		{
			Date:        "2023-08-26",
			MissedHabit: true,
			Completed:   []string{"goal-1", "goal-2"},
			Uncompleted: []string{"goal-3", "goal-4", "goal-5"},
		},
		{
			Date:        "2023-08-25",
			MissedHabit: false,
			Completed:   []string{"goal-1", "goal-2", "goal-3"},
			Uncompleted: []string{"goal-4", "goal-5", "goal-6"},
		},
	}
	json, err := json.Marshal(&response)
	if err != nil {
		t.Fatalf("Failed to write json")
	}

	expectedJsonString := string(expectedJson)
	actualJsonString := string(json)
	if expectedJsonString != actualJsonString {
		t.Fatalf("Response did not match expected value:\n\texpected \"%v\"\n\tgot      \"%v\"", expectedJsonString, actualJsonString)
	}
}
