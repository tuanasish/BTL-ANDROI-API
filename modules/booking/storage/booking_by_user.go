// storage/booking_storage.go
package storage

import (
	"BOOKING_BE/modules/booking/model"

	"gorm.io/gorm"
)

type BookingByUser struct {
	db *gorm.DB
}

func NewBookingByUser(db *gorm.DB) *BookingStorage {
	return &BookingStorage{db: db}
}

// GetBookingsByUserID truy vấn danh sách booking theo user_id
func (s *BookingStorage) GetBookingsByUserID(userID int) ([]model.Booking, error) {
	var bookings []model.Booking
	err := s.db.Where("user_id = ?", userID).Find(&bookings).Error
	if err != nil {
		return nil, err
	}
	return bookings, nil
}
