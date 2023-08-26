package models

type WhoAmIResponseV1 struct {
	Identity Identity `json:"identity"`
}

type HabitsSummaryResponseV1 struct {
	Habits []HabitSummary `json:"habits"`
}

type HabitsForDayResponseV1 struct {
	Completed   []Goal `json:"completed"`
	Uncompleted []Goal `json:"uncompleted"`
}

type GoalsResponseV1 struct {
	Goals []Goal `json:"goals"`
}

type GoalsForDayResponseV1 struct {
	Goals []Goal `json:"goals"`
}
