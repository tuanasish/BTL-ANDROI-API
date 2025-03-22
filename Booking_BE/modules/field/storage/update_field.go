package storage

import (
	"BOOKING_BE/modules/field/model"
	"context"

	"gorm.io/gorm"
)

func (s *StorageField) UpdateField(ctx context.Context, id int, data *model.FieldUpdate) error {
	// Kiểm tra xem sân có tồn tại không
	var field model.Field
	if err := s.db.First(&field, id).Error; err != nil {
		if err == gorm.ErrRecordNotFound {
			return err
		}
		return err
	}

	// Tiến hành cập nhật
	if err := s.db.Model(&field).Updates(data).Error; err != nil {
		return err
	}
	return nil
}
