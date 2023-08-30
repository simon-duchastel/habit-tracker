package v1

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/GoogleCloudPlatform/functions-framework-go/functions"

	"github.com/simon-duchastel/habit-tracker/server/models"
)

func InitGetHabitsSummary() {
	functions.HTTP("GetHabitsSummary", GetHabitsSummary)
}

func GetHabitsSummary(w http.ResponseWriter, r *http.Request) {
	response := models.HabitsSummaryResponseV1{}
	response.Habits = []models.HabitSummary{
		{
			Date:        "2023-08-26",
			Completed:   []string{"goal-1", "goal-2"},
			Uncompleted: []string{"goal-3", "goal-4", "goal-5"},
		},
		{
			Date:        "2023-08-25",
			Completed:   []string{"goal-1", "goal-3"},
			Uncompleted: []string{"goal-2", "goal-4", "goal-5"},
		},
		{
			Date:        "2023-08-24",
			Completed:   []string{"goal-1", "goal-2", "goal-3"},
			Uncompleted: []string{"goal-4", "goal-5"},
		},
		{
			Date:        "2023-08-23",
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{},
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
