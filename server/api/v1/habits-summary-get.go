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
		{
			Date:        "2023-08-22",
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-21",
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-20",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-19",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-18",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-17",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-16",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-15",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-14",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-13",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-12",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-11",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-10",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-09",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-08",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-07",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-06",
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-05",
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{"goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-04",
			Completed:   []string{},
			Uncompleted: []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5", "goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-03",
			Completed:   []string{},
			Uncompleted: []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5", "goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-02",
			Completed:   []string{},
			Uncompleted: []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5", "goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-01",
			Completed:   []string{},
			Uncompleted: []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5", "goal-6", "goal-7"},
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
