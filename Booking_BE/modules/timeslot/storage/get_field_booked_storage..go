package storage

import (
	"BOOKING_BE/modules/timeslot/model"
	"context"

	"gorm.io/gorm"
)

// StorageField đã được định nghĩa (chứa db *gorm.DB)
type StorageField struct {
	db *gorm.DB
}

func NewStorageField(db *gorm.DB) *StorageField {
	return &StorageField{db: db}
}

// Lấy danh sách khung giờ đã được đặt cho một field cụ thể trong ngày xác định
func (s *StorageField) GetBookedTimeSlotsByField(ctx context.Context, fieldID int, date string) ([]*model.TimeSlot, error) {
	var slots []*model.TimeSlot

	err := s.db.Table("TimeSlots").
		Where("field_id = ? AND booking_date = ? AND status = ?", fieldID, date, "booked").
		Find(&slots).Error

	if err != nil {
		return nil, err
	}
	return slots, nil
}
