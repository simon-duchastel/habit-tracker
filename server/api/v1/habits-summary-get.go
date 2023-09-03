package v1

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"golang.org/x/exp/slices"

	"cloud.google.com/go/firestore"
	"github.com/GoogleCloudPlatform/functions-framework-go/functions"

	"github.com/simon-duchastel/habit-tracker/server/database"
	"github.com/simon-duchastel/habit-tracker/server/models"
)

const defaultCount = 20
const minCount = 1
const maxCount = 200
const userIdParam = "userId"
const countParam = "count"
const startingFromParam = "startingFrom"

func InitGetHabitsSummary() {
	functions.HTTP("GetHabitsSummary", GetHabitsSummary)
}

func GetHabitsSummary(w http.ResponseWriter, r *http.Request) {
	// setup database
	context := r.Context()
	var dbClient *firestore.Client
	var err error
	if dbClient, err = database.GetDbClient(context); err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Internal server error connecting to database")
		return
	}
	defer dbClient.Close()

	// get userId from request
	userId := r.URL.Query().Get(userIdParam)
	if !r.URL.Query().Has(userIdParam) || userId == "" {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Error retreiving user id from path")
		return
	}

	// get count from request, or default to 20
	var countString = r.URL.Query().Get(countParam)
	var count int = defaultCount
	if r.URL.Query().Has(countParam) {
		var err error
		if count, err = strconv.Atoi(countString); err != nil {
			w.WriteHeader(http.StatusBadRequest)
			fmt.Fprintf(w,
				"'count' param must be an integer greater than %v and smaller than %v: %v is invalid",
				minCount-1,
				maxCount,
				countString,
			)
			return
		}
	}
	if count < minCount || count > maxCount {
		w.WriteHeader(http.StatusBadRequest)
		fmt.Fprintf(w,
			"'count' param must be an integer greater than %v and smaller than %v: %v is invalid",
			minCount-1,
			maxCount+1,
			countString,
		)
		return
	}

	// get startingFrom from request, or default to the current day
	startingFromString := r.URL.Query().Get(startingFromParam)
	var endOfRange time.Time = time.Now().UTC()
	if r.URL.Query().Has(startingFromParam) {
		var err error
		if endOfRange, err = time.Parse(time.DateOnly, startingFromString); err != nil {
			w.WriteHeader(http.StatusBadRequest)
			fmt.Fprintf(w, "'startingFrom' param must be a valid date of the form YYYY-MM-DD: %v is invalid", startingFromString)
			return
		}
	}

	// get all habits in the requested range
	beginningOfRange := endOfRange.AddDate(0, 0, -1*count)
	var habitRange []database.HabitDay
	if habitRange, err = database.GetHabitRange(context, dbClient, userId, beginningOfRange, endOfRange); err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Error connecting to database while retrieving habits")
		return
	}

	// get all goals active in the requested range
	var goalRange []database.Goal
	if goalRange, err = database.GetGoalsActiveInRange(context, dbClient, userId, beginningOfRange, endOfRange); err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Error connecting to database while retrieving goals")
		return
	}

	// iterate through all days in the range we're returning, accumulating
	// all the days to return
	summaries := []models.HabitSummary{}
	for day := beginningOfRange; day.Before(endOfRange); day = day.AddDate(0, 0, 1) {
		var dayBefore = day.AddDate(0, 0, -1)

		// get the indexes for the habit day summaries we fetched from the database
		currentDayIndex := slices.IndexFunc(habitRange, func(d database.HabitDay) bool {
			return d.Day.Year() == day.Year() && d.Day.Month() == day.Month() && d.Day.Day() == day.Day()
		})
		dayBeforeIndex := slices.IndexFunc(habitRange, func(d database.HabitDay) bool {
			return d.Day.Year() == dayBefore.Year() && d.Day.Month() == dayBefore.Month() && d.Day.Day() == dayBefore.Day()
		})

		// get the habit day for this day and the previous day, or default to nil if the database doesn't have them
		var habitDay *database.HabitDay = nil
		var habitDayBefore *database.HabitDay = nil
		if currentDayIndex > -1 {
			habitDay = &habitRange[dayBeforeIndex]
		}
		if dayBeforeIndex > -1 {
			habitDayBefore = &habitRange[dayBeforeIndex]
		}

		// get the goals active for this day
		var currentGoalsActive []database.Goal
		var dayBeforeGoalsActive []database.Goal
		for i := range goalRange {
			startsBefore := goalRange[i].StartDate.AddDate(0, 0, -1).Before(day)
			endsAfter := goalRange[i].EndDate == nil || goalRange[i].EndDate.After(day)
			isActiveOnDay := goalRange[i].ActiveOn[day.Weekday()]

			if startsBefore && endsAfter && isActiveOnDay {
				currentGoalsActive = append(currentGoalsActive, goalRange[i])
			}
		}
		for i := range goalRange {
			startsBefore := goalRange[i].StartDate.AddDate(0, 0, -1).Before(dayBefore)
			endsAfter := goalRange[i].EndDate == nil || goalRange[i].EndDate.After(dayBefore)
			isActiveOnDay := goalRange[i].ActiveOn[dayBefore.Weekday()]

			if startsBefore && endsAfter && isActiveOnDay {
				dayBeforeGoalsActive = append(dayBeforeGoalsActive, goalRange[i])
			}
		}

		// calculate whether we missed our habit today
		var habitMissed bool
		if habitDayBefore == nil {

		}

		habitSummary := models.HabitSummary{}
		habitSummary.Date = day.Format(time.DateOnly)
		habitSummary.MissedHabit = habitMissed
		if habitDay == nil {
			// handle empty case (db has no record of this day, so no goals were completed)
			habitSummary.Completed = []string{}
			habitSummary.Uncompleted = []string{}
			for i := range currentGoalsActive {
				habitSummary.Uncompleted = append(habitSummary.Uncompleted, currentGoalsActive[i].GoalId)
			}
		} else {
			habitSummary.Completed = habitDay.Completed
			habitSummary.Uncompleted = []string{}
			for i := range currentGoalsActive {
				if !slices.Contains(habitDay.Completed, currentGoalsActive[i].GoalId) {
					habitSummary.Uncompleted = append(habitSummary.Uncompleted, currentGoalsActive[i].GoalId)
				}
			}
		}
		summaries = append(summaries, habitSummary)
	}

	// Return the response
	response := models.HabitsSummaryResponseV1{}
	response.Habits = summaries
	data, err := json.Marshal(&response)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintf(w, "Error writing json response")
		return
	}

	w.WriteHeader(http.StatusOK)
	w.Write(data)
}
