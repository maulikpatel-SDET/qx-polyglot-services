"""R4-11 HOP 2 of 3: middle library in qx-polyglot-services wraps qx-core (qx-evasion-lab). No algorithm named."""
from qx_core.primitives import raw_sign


def sign_order(order: bytes) -> bytes:
    return raw_sign(b"order:" + order)
