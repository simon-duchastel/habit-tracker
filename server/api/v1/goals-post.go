package v1

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/GoogleCloudPlatform/functions-framework-go/functions"

	"github.com/simon-duchastel/habit-tracker/server/models"
)

func InitPostGoals() {
	functions.HTTP("PostGoals", PostGoals)
}

func PostGoals(w http.ResponseWriter, r *http.Request) {
	var request models.GoalsRequestV1
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		fmt.Fprintf(w, "Request body does not match expected schema")
		return
	}

	response := models.GoalsResponseV1{}
	response.Goals = []models.Goal{
		{
			GoalId: "goal-123",
			Title:  "Clean room",
			IsActive: models.DaysActive{
				Monday:    false,
				Tuesday:   false,
				Wednesday: false,
				Thursday:  false,
				Friday:    false,
				Saturday:  true,
				Sunday:    true,
			},
		},
		{
			GoalId: "goal-456",
			Title:  "Run 3 miles",
			IsActive: models.DaysActive{
				Monday:    true,
				Tuesday:   false,
				Wednesday: true,
				Thursday:  false,
				Friday:    true,
				Saturday:  true,
				Sunday:    false,
			},
		},
		{
			GoalId: "goal-789",
			Title:  "Wash 3 dishes",
			IsActive: models.DaysActive{
				Monday:    true,
				Tuesday:   true,
				Wednesday: true,
				Thursday:  true,
				Friday:    true,
				Saturday:  true,
				Sunday:    true,
			},
		},
	}
	data, err := json.Marshal(&response)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Error writing json response")
		return
	}

	w.WriteHeader(http.StatusOK)
	w.Write(data)
}
