package biz

import (
	"BOOKING_BE/modules/field/model"
	"context"
	"errors"
)

type GetCourtList interface {
	GetCourtsByFieldID(ctx context.Context, fieldID int) ([]model.Court, error)
}

type GetCourtListBiz struct {
	store GetCourtList
}

func NewGetCourtListBiz(store GetCourtList) *GetCourtListBiz {
	return &GetCourtListBiz{store: store}
}

// GetCourts lấy số lượng court và danh sách id của các court theo fieldID
func (biz *GetCourtListBiz) GetCourts(ctx context.Context, fieldID int) (int, []int, error) {
	if fieldID == 0 {
		return 0, nil, errors.New("field id không hợp lệ")
	}

	courts, err := biz.store.GetCourtsByFieldID(ctx, fieldID)
	if err != nil {
		return 0, nil, err
	}

	courtIDs := make([]int, len(courts))
	for i, court := range courts {
		courtIDs[i] = court.ID
	}

	return len(courts), courtIDs, nil
}
