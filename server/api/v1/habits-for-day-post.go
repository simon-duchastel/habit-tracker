package v1

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/GoogleCloudPlatform/functions-framework-go/functions"

	"github.com/simon-duchastel/habit-tracker/server/models"
)

func InitPostHabitsForDay() {
	functions.HTTP("PostHabitsForDay", PostHabitsForDay)
}

func PostHabitsForDay(w http.ResponseWriter, r *http.Request) {
	var request models.HabitsForDayRequestV1
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		fmt.Fprintf(w, "Request body does not match expected schema")
		return
	}

	response := models.HabitsForDayResponseV1{}
	response.Completed = []models.GoalSummary{
		{
			GoalId: "goal-123",
			Title:  "Clean room",
		},
		{
			GoalId: "goal-456",
			Title:  "Run 3 miles",
		},
	}
	response.Uncompleted = []models.GoalSummary{
		{
			GoalId: "goal-789",
			Title:  "Wash 3 dishes",
		},
	}

	data, err := json.Marshal(&response)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Error writing json response")
		return
	}

	w.WriteHeader(http.StatusOK)
	w.Write(data)
}
