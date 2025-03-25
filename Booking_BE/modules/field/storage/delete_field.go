package storage

import (
	"BOOKING_BE/modules/field/model"
	"context"
	"errors"

	"gorm.io/gorm"
)

func (s *StorageField) DeleteField(ctx context.Context, id int) error {
	// Kiểm tra xem sân có tồn tại không
	var field model.Field
	if err := s.db.First(&field, id).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return err
		}
		return err
	}

	// Xoá mềm bằng cách cập nhật trạng thái (nếu có cột `deleted_at` thì có thể dùng `Delete(&field)`)
	if err := s.db.Delete(&field).Error; err != nil {
		return err
	}
	return nil
}
