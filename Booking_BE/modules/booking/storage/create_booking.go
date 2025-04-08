package storage

import (
	"BOOKING_BE/modules/booking/model"
	"time"

	"gorm.io/gorm"
)

type BookingStorage struct {
	db *gorm.DB
}

func NewBookingStorage(db *gorm.DB) *BookingStorage {
	return &BookingStorage{db: db}
}

func (s *BookingStorage) CreateBookingCourt(data *model.Booking) error {
	// var slot *modules.TimeSlotCreate
	// slot.FieldID = data.FieldID
	// slot.BookingDate = data.CreatedAt
	// slot.StartTime = data.StartTime
	// slot.EndTime = data.EndTime
	if err := s.db.Create(data).Error; err != nil {
		return err
	}
	// if err := s.db.Create(slot).Error; err != nil {
	// 	return err
	// }
	return nil
}

func (s *BookingStorage) IsCourtAvailable(fieldID, courtID uint, date string, startTime, endTime time.Time) (bool, error) {
	var count int64
	err := s.db.Model(&model.Booking{}).
		Where("field_id = ? AND court_id = ? AND date = ? AND ((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?))",
			fieldID, courtID, date, endTime, startTime, startTime, endTime).
		Count(&count).Error

	if err != nil {
		return false, err
	}

	return count == 0, nil
}
