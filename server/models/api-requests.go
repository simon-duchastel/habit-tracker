package models

type HabitsForDayRequestV1 struct {
	Completed   []GoalId `json:"completed"`
	Uncompleted []GoalId `json:"uncompleted"`
}

type GoalsRequestV1 struct {
	Goal []Goal `json:"goal"`
}
