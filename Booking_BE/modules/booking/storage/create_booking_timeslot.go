package storage

import (
	"BOOKING_BE/modules/booking/model"                // chứa Booking và BookingTimeSlotRequest
	timeslotModel "BOOKING_BE/modules/timeslot/model" // chứa TimeSlot
	"context"
	"time"

	"gorm.io/gorm"
)

type BookingTimeSlotStorage struct {
	db *gorm.DB
}

func NewBookingTimeSlotStorage(db *gorm.DB) *BookingTimeSlotStorage {
	return &BookingTimeSlotStorage{db: db}
}

func (s *BookingTimeSlotStorage) CreateBookingTimeSlot(ctx context.Context, data *model.BookingTimeSlotRequest) error {
	// Bắt đầu transaction với context
	tx := s.db.WithContext(ctx).Begin()
	if tx.Error != nil {
		return tx.Error
	}

	// Parse date cho TimeSlot
	bookingDate, err := time.Parse("2006-01-02", data.Date)
	if err != nil {
		tx.Rollback()
		return err
	}

	// Tạo record Booking
	bookingRecord := model.Booking{
		UserID:     data.UserID,
		FieldID:    data.FieldID,
		CourtID:    data.CourtID,
		Date:       data.Date,
		StartTime:  data.StartTime,
		EndTime:    data.EndTime,
		Status:     "confirmed",     // Thay đổi status nếu cần
		TotalPrice: data.TotalPrice, // Tổng tiền tính từ request
	}

	if err := tx.Create(&bookingRecord).Error; err != nil {
		tx.Rollback()
		return err
	}

	// Tạo record TimeSlot
	timeslotRecord := timeslotModel.TimeSlot{
		FieldID:     int(data.FieldID), // Ép kiểu nếu cần
		BookingDate: bookingDate,
		StartTime:   data.StartTime,
		EndTime:     data.EndTime,
		Status:      "booked", // Mặc định là booked khi có booking
	}

	if err := tx.Create(&timeslotRecord).Error; err != nil {
		tx.Rollback()
		return err
	}

	// Commit transaction nếu mọi thao tác thành công
	return tx.Commit().Error
}
