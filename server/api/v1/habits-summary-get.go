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
			Completed:   2,
			Uncompleted: 3,
		},
		{
			Date:        "2023-08-25",
			Completed:   3,
			Uncompleted: 3,
		},
		{
			Date:        "2023-08-25",
			Completed:   3,
			Uncompleted: 3,
		},
		{
			Date:        "2023-08-25",
			Completed:   3,
			Uncompleted: 3,
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
