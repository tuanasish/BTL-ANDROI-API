package biz

import (
	"BOOKING_BE/common"
	"BOOKING_BE/modules/field/model"
	"context"
)

type GetlistField interface {
	GetListFields(ctx context.Context, paging *common.Paging) ([]model.Field, error)
}

type GetListFieldBiz struct {
	store GetlistField
}

func NewGetListFieldBiz(store GetlistField) *GetListFieldBiz {
	return &GetListFieldBiz{store: store}
}

func (biz *GetListFieldBiz) ListField(ctx context.Context, paging *common.Paging) ([]model.Field, error) {
	paging.Process()
	fields, err := biz.store.GetListFields(ctx, paging)
	if err != nil {
		return nil, err
	}
	return fields, nil
}
