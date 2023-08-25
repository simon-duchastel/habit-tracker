package server

import (
	"net/http"

	v1 "github.com/simon-duchastel/habit-tracker/server/api/v1"
)

// Proxy functions to allow Function deployments (functions must be callable from root)

func GetWhoAmIV1(w http.ResponseWriter, r *http.Request) {
	v1.GetWhoAmI(w, r)
}

func GetHabitsSummaryV1(w http.ResponseWriter, r *http.Request) {
	v1.GetHabitsSummary(w, r)
}

func GetHabitsForDayV1(w http.ResponseWriter, r *http.Request) {
	v1.GetHabitsForDay(w, r)
}

func PostHabitsForDayV1(w http.ResponseWriter, r *http.Request) {
	v1.PostHabitsForDay(w, r)
}

func GetGoalsV1(w http.ResponseWriter, r *http.Request) {
	v1.GetGoals(w, r)
}

func PostGoalsV1(w http.ResponseWriter, r *http.Request) {
	v1.PostGoals(w, r)
}
