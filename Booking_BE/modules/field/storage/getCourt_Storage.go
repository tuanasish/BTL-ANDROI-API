package storage

import (
	"BOOKING_BE/modules/field/model"
	"context"
)

// GetCourtsByFieldID trả về danh sách court theo fieldID
func (s *StorageField) GetCourtsByFieldID(ctx context.Context, fieldID int) ([]model.Court, error) {
	var courts []model.Court
	if err := s.db.WithContext(ctx).Where("field_id = ?", fieldID).Find(&courts).Error; err != nil {
		return nil, err
	}
	return courts, nil
}
