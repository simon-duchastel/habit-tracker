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
			MissedHabit: false,
			Completed:   []string{"goal-1", "goal-2"},
			Uncompleted: []string{"goal-3", "goal-4", "goal-5"},
		},
		{
			Date:        "2023-08-25",
			MissedHabit: false,
			Completed:   []string{"goal-1", "goal-3"},
			Uncompleted: []string{"goal-2", "goal-4", "goal-5"},
		},
		{
			Date:        "2023-08-24",
			MissedHabit: false,
			Completed:   []string{"goal-1", "goal-2", "goal-3"},
			Uncompleted: []string{"goal-4", "goal-5"},
		},
		{
			Date:        "2023-08-23",
			MissedHabit: true,
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-22",
			MissedHabit: true,
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-21",
			MissedHabit: false,
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-20",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-19",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-18",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-17",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-16",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-15",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-14",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-13",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-12",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-11",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-10",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-09",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-08",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-07",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-06",
			MissedHabit: false,
			Completed:   []string{},
			Uncompleted: []string{},
		},
		{
			Date:        "2023-08-05",
			MissedHabit: false,
			Completed:   []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5"},
			Uncompleted: []string{"goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-04",
			MissedHabit: true,
			Completed:   []string{},
			Uncompleted: []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5", "goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-03",
			MissedHabit: true,
			Completed:   []string{},
			Uncompleted: []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5", "goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-02",
			MissedHabit: true,
			Completed:   []string{},
			Uncompleted: []string{"goal-1", "goal-2", "goal-3", "goal-4", "goal-5", "goal-6", "goal-7"},
		},
		{
			Date:        "2023-08-01",
			MissedHabit: false,
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
