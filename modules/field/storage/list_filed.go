package storage

import (
	"BOOKING_BE/common"
	"BOOKING_BE/modules/field/model"
	"context"
)

func (s *StorageField) GetListFields(ctx context.Context, paging *common.Paging) ([]model.Field, error) {
	var fields []model.Field

	// Đếm tổng số sân
	if err := s.db.Model(&model.Field{}).Count(&paging.Total).Error; err != nil {
		return nil, err
	}

	// Lấy danh sách sân có phân trang
	if err := s.db.Offset((paging.Page - 1) * paging.Limit).
		Limit(paging.Limit).
		Find(&fields).Error; err != nil {
		return nil, err
	}

	return fields, nil
}
