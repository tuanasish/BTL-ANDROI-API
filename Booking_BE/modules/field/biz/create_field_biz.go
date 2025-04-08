package biz

import (
	"BOOKING_BE/modules/field/model"
	"context"
)

type CreateField interface {
	CreateField(ctx context.Context, data *model.FieldCreate) error
}

type CreateFieldBiz struct {
	store CreateField
}

func NewCreateFieldBiz(store CreateField) *CreateFieldBiz {
	return &CreateFieldBiz{store: store}
}

// func (biz *CreateFieldBiz) ValidateFieldData(data *model.FieldCreate) error {
// 	if data.Name == "" {
// 		return errors.New("tên sân không được để trống")
// 	}
// 	if len(data.Name) < 3 || len(data.Name) > 100 {
// 		return errors.New("tên sân phải có từ 3 đến 100 ký tự")
// 	}
// 	if data.Location == "" {
// 		return errors.New("địa điểm không được để trống")
// 	}
// 	if data.Type == nil {
// 		return errors.New("loại sân không hợp lệ")
// 	}
// 	if data.Price == "" {
// 		return errors.New("giá không được để trống")
// 	}
// 	return nil
// }

func (biz *CreateFieldBiz) CreateField(ctx context.Context, data *model.FieldCreate) error {
	// Kiểm tra dữ liệu
	// if err := biz.ValidateFieldData(data); err != nil {
	// 	return err
	// }

	// Thực hiện lưu vào rB
	if err := biz.store.CreateField(ctx, data); err != nil {
		return err
	}

	return nil
}
