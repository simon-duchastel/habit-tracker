package models

type HabitsForDayRequestV1 struct {
	Completed   []Goal `json:"completed"`
	Uncompleted []Goal `json:"Uncompleted"`
}

type GoalsRequestV1 struct {
	Goal []Goal `json:"goal"`
}
