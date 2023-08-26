package models

type HabitsForDayRequestV1 struct {
	completed   []Goal
	uncompleted []Goal
}

type GoalsRequestV1 struct {
	goal []Goal
}
