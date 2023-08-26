package models

type Identity struct {
	userId string
}

type HabitSummary struct {
	date        string
	completed   int
	uncompleted int
}

type Goal struct {
	title  string
	goalId string
}
