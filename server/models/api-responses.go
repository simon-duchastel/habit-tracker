package models

type WhoAmIResponseV1 struct {
	identity Identity
}

type HabitsSummaryResponseV1 struct {
	habits []HabitSummary
}

type HabitsForDayResponseV1 struct {
	completed   []Goal
	uncompleted []Goal
}

type GoalsResponseV1 struct {
	goals []Goal
}

type GoalsForDayResponseV1 struct {
	goals []Goal
}
