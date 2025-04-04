package biz

import (
	"BOOKING_BE/modules/field/model"
	"context"
	"errors"
)

type UpdateField interface {
	UpdateField(ctx context.Context, id int, data *model.FieldUpdate) error
}

type UpdateFieldBiz struct {
	store UpdateField
}

func NewUpdateFieldBiz(store UpdateField) *UpdateFieldBiz {
	return &UpdateFieldBiz{store: store}
}

func (biz *UpdateFieldBiz) UpdateField(ctx context.Context, id int, data *model.FieldUpdate) error {
	// Kiểm tra id hợp lệ
	if id <= 0 {
		return errors.New("id không hợp lệ")
	}

	// Validate dữ liệu
	if data.Name != "" && (len(data.Name) < 3 || len(data.Name) > 100) {
		return errors.New("tên sân phải có từ 3 đến 100 ký tự")
	}

	// if data.Price != "" && len(data.Price) < 3 {
	// 	return errors.New("giá không hợp lệ")
	// }

	// Gọi storage để cập nhật dữ liệu
	if err := biz.store.UpdateField(ctx, id, data); err != nil {
		return err
	}
	return nil
}
