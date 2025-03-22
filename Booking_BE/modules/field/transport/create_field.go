package transport

import (
	"BOOKING_BE/common"
	"BOOKING_BE/modules/field/biz"
	"BOOKING_BE/modules/field/model"
	"BOOKING_BE/modules/field/storage"
	"context"
	"net/http"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func CreateField(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		var data model.FieldCreate

		// Validate dữ liệu đầu vào bằng Gin binding
		if err := c.ShouldBindJSON(&data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		// Gọi business logic để xử lý
		fieldStorage := storage.NewStorageField(db)
		fieldBiz := biz.NewCreateFieldBiz(fieldStorage)

		if err := fieldBiz.CreateField(context.Background(), &data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, gin.H{"message": common.SimpleSuccessResponse(&data)})
	}
}
