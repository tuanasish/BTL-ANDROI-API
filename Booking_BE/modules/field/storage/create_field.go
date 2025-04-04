package storage

import (
	"BOOKING_BE/modules/field/model"
	"context"
)

func (s *StorageField) CreateField(ctx context.Context, data *model.FieldCreate) error {

	if err := s.db.Create(data).Error; err != nil {
		return err
	}
	return nil
}
