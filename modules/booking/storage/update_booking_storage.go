package storage

import (
	"BOOKING_BE/modules/booking/model"

	"gorm.io/gorm"
)

type BookingStorageUpdate struct {
	db *gorm.DB
}

func NewUpdateBookingStorage(db *gorm.DB) *BookingStorageUpdate {
	return &BookingStorageUpdate{db: db}
}

// UpdateBooking cập nhật thông tin booking dựa vào ID
func (s *BookingStorageUpdate) UpdateBooking(bookingID int, data *model.UpdateBooking) error {
	updateData := make(map[string]interface{})

	if data.FieldID != nil {
		updateData["field_id"] = *data.FieldID
	}
	if data.CourtID != nil {
		updateData["court_id"] = *data.CourtID
	}
	if data.Date != nil {
		updateData["date"] = *data.Date
	}
	if data.StartTime != nil {
		updateData["start_time"] = *data.StartTime
	}
	if data.EndTime != nil {
		updateData["end_time"] = *data.EndTime
	}
	if data.Status != nil {
		updateData["booking_status"] = *data.Status
	}
	if data.TotalPrice != nil {
		updateData["total_price"] = *data.TotalPrice
	}

	if len(updateData) == 0 {
		return nil // Không có gì để cập nhật
	}

	return s.db.Model(&model.Booking{}).Where("booking_id = ?", bookingID).Updates(updateData).Error
}
