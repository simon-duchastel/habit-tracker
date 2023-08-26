package models

import (
	"encoding/json"
	"testing"
)

func TestHabitsSummaryResponseV1(t *testing.T) {
	expectedJson := []byte(`{"habits":[{"date":"2023-08-26","completed":2,"uncompleted":3},{"date":"2023-08-25","completed":3,"uncompleted":3}]}`)

	var response HabitsSummaryResponseV1
	response.Habits = []HabitSummary{
		{
			Date:        "2023-08-26",
			Completed:   2,
			Uncompleted: 3,
		},
		{
			Date:        "2023-08-25",
			Completed:   3,
			Uncompleted: 3,
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
