package models

type Identity struct {
	UserId string `json:"UserId"`
}

type HabitSummary struct {
	Date        string `json:"date"`
	Completed   int    `json:"completed"`
	Uncompleted int    `json:"uncompleted"`
}

type Goal struct {
	Title  string `json:"title"`
	GoalId string `json:"goalId"`
}
