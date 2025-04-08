package biz

import (
	"BOOKING_BE/modules/field/model"
	"context"
	"errors"
)

type GetFieldByID interface {
	GetFieldByID(ctx context.Context, id int) (*model.Field, error)
}

type GetFieldBiz struct {
	store GetFieldByID
}

func NewGetFieldBiz(store GetFieldByID) *GetFieldBiz {
	return &GetFieldBiz{store: store}
}

func (biz *GetFieldBiz) GetFieldByID(ctx context.Context, id int) (*model.Field, error) {
	// Kiểm tra tính hợp lệ của ID
	if id <= 0 {
		return nil, errors.New("id không hợp lệ")
	}

	// Gọi hàm lấy thông tin sân từ storage
	field, err := biz.store.GetFieldByID(ctx, id)
	if err != nil {
		return nil, err
	}
	return field, nil
}
