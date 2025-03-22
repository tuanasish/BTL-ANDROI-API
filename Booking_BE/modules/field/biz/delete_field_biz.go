package biz

import (
	"context"
	"errors"
)

type DeleteField interface {
	DeleteField(ctx context.Context, id int) error
}

type DeleteFieldBiz struct {
	store DeleteField
}

func NewDeleteFieldBiz(store DeleteField) *DeleteFieldBiz {
	return &DeleteFieldBiz{store: store}
}

func (biz *DeleteFieldBiz) DeleteField(ctx context.Context, id int) error {
	// Kiểm tra id hợp lệ
	if id <= 0 {
		return errors.New("id không hợp lệ")
	}

	// Gọi storage để xoá
	if err := biz.store.DeleteField(ctx, id); err != nil {
		return err
	}
	return nil
}
