package model

import (
	"time"
)

// TimeSlot model đại diện cho bảng TimeSlots trong cơ sở dữ liệu.
type TimeSlot struct {
	SlotID      int       `json:"slot_id" gorm:"column:slot_id;primaryKey;autoIncrement"`
	FieldID     int       `json:"field_id" gorm:"column:field_id;not null"`
	BookingDate time.Time `json:"booking_date" gorm:"column:booking_date;type:date;not null"`
	StartTime   string    `json:"start_time" gorm:"column:start_time"` // Chuyển thành string
	EndTime     string    `json:"end_time" gorm:"column:end_time"`     // Chuyển thành string
	Status      string    `json:"status" gorm:"column:status;type:enum('available','booked');default:'available'"`
}

// TableName chỉ định tên bảng trong cơ sở dữ liệu.
func (t TimeSlot) TableName() string {
	return "TimeSlots"
}

// TimeSlotCreate dùng cho thao tác tạo mới TimeSlot.
type TimeSlotCreate struct {
	FieldID     int       `json:"field_id" gorm:"column:field_id" binding:"required"`
	BookingDate time.Time `json:"booking_date" gorm:"column:booking_date;type:date" binding:"required" time_format:"2006-01-02"`
	StartTime   string    `json:"start_time" gorm:"column:start_time"` // Chuyển thành string
	EndTime     string    `json:"end_time" gorm:"column:end_time"`     // Chuyển thành string
	// Status có thể được truyền vào nếu muốn đặt trạng thái khác mặc định (available)
	Status string `json:"status" gorm:"column:status;type:enum('available','booked')" binding:"omitempty,oneof=available booked"`
}

// TableName chỉ định tên bảng cho TimeSlotCreate.
func (t TimeSlotCreate) TableName() string {
	return TimeSlot{}.TableName()
}

// TimeSlotUpdate dùng cho thao tác cập nhật TimeSlot.
type TimeSlotUpdate struct {
	FieldID     int       `json:"field_id" gorm:"column:field_id"`
	BookingDate time.Time `json:"booking_date" gorm:"column:booking_date;type:date"`
	StartTime   string    `json:"start_time" gorm:"column:start_time"` // Chuyển thành string
	EndTime     string    `json:"end_time" gorm:"column:end_time"`     // Chuyển thành string
	Status      string    `json:"status" gorm:"column:status;type:enum('available','booked')"`
}

// TableName chỉ định tên bảng cho TimeSlotUpdate.
func (t TimeSlotUpdate) TableName() string {
	return TimeSlot{}.TableName()
}
