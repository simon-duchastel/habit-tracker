package models

type Identity struct {
	UserId string `json:"userId"`
}

type HabitSummary struct {
	Date        string `json:"date"`
	Completed   int    `json:"completed"`
	Uncompleted int    `json:"uncompleted"`
}

type Goal struct {
	Title    string     `json:"title"`
	GoalId   string     `json:"goalId"`
	IsActive DaysActive `json:"isActive"`
}

type GoalId struct {
	GoalId string `json:"goalId"`
}

type GoalSummary struct {
	GoalId string `json:"goalId"`
	Title  string `json:"title"`
}

type DaysActive struct {
	Monday    bool `json:"monday"`
	Tuesday   bool `json:"tuesday"`
	Wednesday bool `json:"wednesday"`
	Thursday  bool `json:"thursday"`
	Friday    bool `json:"friday"`
	Saturday  bool `json:"saturday"`
	Sunday    bool `json:"sunday"`
}
