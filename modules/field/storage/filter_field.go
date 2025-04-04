package storage

import (
	"BOOKING_BE/modules/field/model"
	"context"
)

func (s *StorageField) FilterFields(ctx context.Context, location string, fieldType *model.TypeField, minPrice, maxPrice int) ([]model.Field, error) {
	var fields []model.Field
	query := s.db.Table(model.Field{}.TableName())

	// Lọc theo location nếu có
	if location != "" {
		query = query.Where("location = ?", location)
	}

	// Lọc theo type nếu có
	if fieldType != nil {
		query = query.Where("type = ?", fieldType)
	}

	// Lọc theo price nếu có min và max
	if minPrice > 0 && maxPrice > 0 {
		query = query.Where("CAST(price AS UNSIGNED) BETWEEN ? AND ?", minPrice, maxPrice)
	} else if minPrice > 0 {
		query = query.Where("CAST(price AS UNSIGNED) >= ?", minPrice)
	} else if maxPrice > 0 {
		query = query.Where("CAST(price AS UNSIGNED) <= ?", maxPrice)
	}

	// Thực hiện truy vấn
	if err := query.Find(&fields).Error; err != nil {
		return nil, err
	}

	return fields, nil
}
