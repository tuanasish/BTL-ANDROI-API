package transport

import (
	"BOOKING_BE/common"
	"BOOKING_BE/modules/field/biz"
	"BOOKING_BE/modules/field/storage"
	"context"
	"net/http"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func ListField(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		var paging common.Paging
		if err := c.ShouldBindQuery(&paging); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Request sai định dạng"})
			return
		}

		fieldStorage := storage.NewStorageField(db)
		fieldBiz := biz.NewGetListFieldBiz(fieldStorage)

		fields, err := fieldBiz.ListField(context.Background(), &paging)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, common.NewSuccessResponse(fields, paging, nil))
	}
}
