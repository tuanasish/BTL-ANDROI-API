package storage

import (
	"BOOKING_BE/modules/field/model"
	"context"
	"errors"

	"gorm.io/gorm"
)

func (s *StorageField) GetFieldByID(ctx context.Context, id int) (*model.Field, error) {
	var data *model.Field

	// Sử dụng WithContext để truyền context, thực hiện truy vấn với Where và First
	err := s.db.WithContext(ctx).Where("field_id = ?", id).First(&data).Error
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, errors.New("field không tồn tại")
		}
		return nil, err
	}

	return data, nil
}
