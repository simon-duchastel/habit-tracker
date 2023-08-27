package models

type WhoAmIResponseV1 struct {
	Identity Identity `json:"identity"`
}

type HabitsSummaryResponseV1 struct {
	Habits []HabitSummary `json:"habits"`
}

type HabitsForDayResponseV1 struct {
	Completed   []GoalSummary `json:"completed"`
	Uncompleted []GoalSummary `json:"uncompleted"`
}

type GoalsResponseV1 struct {
	Goals []Goal `json:"goals"`
}
