package biz

import (
	"BOOKING_BE/modules/field/model"
	"context"
)

type FilterFields interface {
	FilterFields(ctx context.Context, location string, fieldType *model.TypeField, minPrice, maxPrice int) ([]model.Field, error)
}

type FilterFieldsBiz struct {
	store FilterFields
}

func NewFilterFieldsBiz(store FilterFields) *FilterFieldsBiz {
	return &FilterFieldsBiz{store: store}
}

func (biz *FilterFieldsBiz) FilterFields(ctx context.Context, location string, fieldType *model.TypeField, minPrice, maxPrice int) ([]model.Field, error) {
	// Gọi storage để lấy danh sách field theo điều kiện
	fields, err := biz.store.FilterFields(ctx, location, fieldType, minPrice, maxPrice)
	if err != nil {
		return nil, err
	}
	return fields, nil
}
