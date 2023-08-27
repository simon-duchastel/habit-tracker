package v1

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/GoogleCloudPlatform/functions-framework-go/functions"

	"github.com/simon-duchastel/habit-tracker/server/models"
)

func InitGetWhoAmI() {
	functions.HTTP("GetWhoAmI", GetWhoAmI)
}

func GetWhoAmI(w http.ResponseWriter, r *http.Request) {
	response := models.WhoAmIResponseV1{}
	response.Identity.UserId = "user-123"

	data, err := json.Marshal(&response)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Error writing json response")
		return
	}

	w.WriteHeader(http.StatusOK)
	w.Write(data)
}
