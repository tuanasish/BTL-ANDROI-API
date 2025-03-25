package model

import (
	"time"
)

type Booking struct {
	ID         uint       `json:"booking_id" gorm:"column:booking_id;primaryKey;autoIncrement"`
	UserID     uint       `json:"user_id" gorm:"column:user_id;not null"`
	FieldID    uint       `json:"field_id" gorm:"column:field_id;not null"`
	CourtID    uint       `json:"court_id" gorm:"column:court_id;not null"`
	Date       string     `json:"date" gorm:"column:date;type:date;not null"`
	StartTime  string     `json:"start_time" gorm:"column:start_time;type:time;not null"`
	EndTime    string     `json:"end_time" gorm:"column:end_time;type:time;not null"`
	Status     string     `json:"status" gorm:"column:booking_status;type:enum('pending','confirmed','cancelled');default:'pending'"`
	TotalPrice float64    `json:"total_price" gorm:"column:total_price;type:decimal(10,2)"`
	CreatedAt  *time.Time `json:"created_at" gorm:"column:created_at;autoCreateTime"`
	UpdatedAt  *time.Time `json:"updated_at" gorm:"column:updated_at;autoUpdateTime"`
}

// TableName chỉ định tên bảng trong DB
func (Booking) TableName() string {
	return "Bookings"
}

type UpdateBooking struct {
	FieldID    *uint    `json:"field_id,omitempty"`
	CourtID    *uint    `json:"court_id,omitempty"`
	Date       *string  `json:"date,omitempty"`
	StartTime  *string  `json:"start_time,omitempty"`
	EndTime    *string  `json:"end_time,omitempty"`
	Status     *string  `json:"status,omitempty"`
	TotalPrice *float64 `json:"total_price,omitempty"`
}

func (s UpdateBooking) TableName() string {
	return Booking{}.TableName()
}
