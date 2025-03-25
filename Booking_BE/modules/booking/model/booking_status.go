package model

import (
	"database/sql/driver"
	"encoding/json"
	"errors"
	"fmt"
	"strings"
)

type BookingStatus int

// "pending", "confirmed", "cancelled"
const (
	BookingStatusPending BookingStatus = iota
	BookingStatusConfirmed
	BookingStatusCancelled
)

var allBookingStatus = [3]string{
	"pending",   // BookingStatusPending
	"confirmed", // BookingStatusConfirmed
	"cancelled", // BookingStatusCancelled
}

func (status BookingStatus) String() string {
	if int(status) < len(allBookingStatus) {
		return allBookingStatus[status]
	}
	return "Unknown"
}

// Parse a string to BookingStatus
func parseStrBookingStatus(s string) (BookingStatus, error) {
	for i := range allBookingStatus {
		if s == allBookingStatus[i] {
			return BookingStatus(i), nil
		}
	}
	return BookingStatus(0), fmt.Errorf("invalid BookingStatus value: %s", s)
}

// Scan converts a value from the database to BookingStatus
func (status *BookingStatus) Scan(value interface{}) error {
	if value == nil {
		*status = BookingStatusPending // Default value if NULL
		return nil
	}
	switch v := value.(type) {
	case string:
		s, err := parseStrBookingStatus(v)
		if err != nil {
			return err
		}
		*status = s
		return nil
	case []byte:
		s, err := parseStrBookingStatus(string(v))
		if err != nil {
			return fmt.Errorf("error parsing BookingStatus: %v", err)
		}
		*status = s
		return nil
	default:
		return errors.New("fail to scan from database: invalid type")
	}
}

// MarshalJSON converts BookingStatus to its JSON representation (as a string)
func (status BookingStatus) MarshalJSON() ([]byte, error) {
	return json.Marshal(status.String())
}

// Value converts BookingStatus to a driver.Value for saving in the database
func (status BookingStatus) Value() (driver.Value, error) {
	if int(status) >= len(allBookingStatus) {
		return nil, errors.New("invalid BookingStatus")
	}
	return allBookingStatus[status], nil
}

// UnmarshalJSON converts JSON data back to BookingStatus
func (status *BookingStatus) UnmarshalJSON(data []byte) error {
	str := strings.Trim(string(data), "\"")
	v, err := parseStrBookingStatus(str)
	if err != nil {
		return err
	}
	*status = v
	return nil
}
