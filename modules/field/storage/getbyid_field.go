package storage

import (
	"BOOKING_BE/modules/field/model"
	"context"
	"errors"

	"gorm.io/gorm"
)

func (s *StorageField) GetFieldByID(ctx context.Context, id int) (*model.Field, error) {
	var field model.Field

	// Tìm sân theo ID
	if err := s.db.First(&field, id).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, err
		}
		return nil, err
	}

	return &field, nil
}
