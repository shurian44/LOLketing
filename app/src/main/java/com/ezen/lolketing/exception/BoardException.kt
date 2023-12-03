package com.ezen.lolketing.exception

sealed class BoardException(
    override val message: String?
) : Throwable() {
    object SearchError: BoardException("게시글 정보를 불러오는데 실패하였습니다")
}